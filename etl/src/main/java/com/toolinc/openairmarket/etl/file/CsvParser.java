package com.toolinc.openairmarket.etl.file;

import static com.google.common.base.Preconditions.checkState;

import com.google.auto.value.AutoValue;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/** Helper class for parsing a file in CSV format. */
@AutoValue
public abstract class CsvParser {

  public static final int INITIAL_READ_SIZE = 128;

  abstract char separator();

  abstract char quoteChar();

  abstract char escape();

  abstract char nullChar();

  abstract boolean strictQuotes();

  abstract boolean ignoreLeadingWhiteSpace();

  private String pending;
  private boolean inField = false;

  public String[] parseLineMulti(String nextLine) throws IOException {
    return parseLine(nextLine, true);
  }

  public String[] parseLine(String nextLine) throws IOException {
    return parseLine(nextLine, false);
  }

  public boolean isPending() {
    return pending != null;
  }

  /**
   * Parses an incoming String and returns an array of elements.
   *
   * @param nextLine the string to parse
   * @return the comma-tokenized list of elements, or null if nextLine is null
   * @throws IOException if bad things happen during the read
   */
  private String[] parseLine(String nextLine, boolean multi) throws IOException {
    if (!multi && pending != null) {
      pending = null;
    }
    if (nextLine == null) {
      if (pending != null) {
        String s = pending;
        pending = null;
        return new String[] {s};
      } else {
        return null;
      }
    }
    List<String> tokensOnThisLine = new ArrayList<String>();
    StringBuilder sb = new StringBuilder(INITIAL_READ_SIZE);
    boolean inQuotes = false;
    if (pending != null) {
      sb.append(pending);
      pending = null;
      inQuotes = true;
    }
    for (int i = 0; i < nextLine.length(); i++) {
      char c = nextLine.charAt(i);
      if (c == escape()) {
        if (isNextCharacterEscapable(nextLine, inQuotes || inField, i)) {
          sb.append(nextLine.charAt(i + 1));
          i++;
        }
      } else if (c == quoteChar()) {
        if (isNextCharacterEscapedQuote(nextLine, inQuotes || inField, i)) {
          sb.append(nextLine.charAt(i + 1));
          i++;
        } else {
          if (!strictQuotes()) {
            if (i > 2
                && nextLine.charAt(i - 1) != separator()
                && nextLine.length() > (i + 1)
                && nextLine.charAt(i + 1) != separator()) {
              if (ignoreLeadingWhiteSpace() && sb.length() > 0 && isAllWhiteSpace(sb)) {
                sb.setLength(0);
              } else {
                sb.append(c);
              }
            }
          }
          inQuotes = !inQuotes;
        }
        inField = !inField;
      } else if (c == separator() && !inQuotes) {
        tokensOnThisLine.add(sb.toString());
        sb.setLength(0);
        inField = false;
      } else {
        if (!strictQuotes() || inQuotes) {
          sb.append(c);
          inField = true;
        }
      }
    }
    if (inQuotes) {
      if (multi) {
        sb.append("\n");
        pending = sb.toString();
        sb = null;
      } else {
        throw new IOException("Un-terminated quoted field at end of csv line.");
      }
    }
    if (sb != null) {
      tokensOnThisLine.add(sb.toString());
    }
    return tokensOnThisLine.toArray(new String[tokensOnThisLine.size()]);
  }

  private boolean isNextCharacterEscapedQuote(String nextLine, boolean inQuotes, int i) {
    return inQuotes && nextLine.length() > (i + 1) && nextLine.charAt(i + 1) == quoteChar();
  }

  protected boolean isNextCharacterEscapable(String nextLine, boolean inQuotes, int i) {
    return inQuotes
        && nextLine.length() > (i + 1)
        && (nextLine.charAt(i + 1) == quoteChar() || nextLine.charAt(i + 1) == escape());
  }

  protected boolean isAllWhiteSpace(CharSequence sb) {
    boolean result = true;
    for (int i = 0; i < sb.length(); i++) {
      char c = sb.charAt(i);

      if (!Character.isWhitespace(c)) {
        return false;
      }
    }
    return result;
  }

  /**
   * Creates a new instance of {@link Builder} with the default values.
   *
   * @return {@link Builder}.
   */
  public static final Builder builder() {
    return new AutoValue_CsvParser.Builder()
        .setEscape(Builder.ESCAPE)
        .setSeparator(Builder.SEPARATOR)
        .setQuoteChar(Builder.QUOTE_CHAR)
        .setNullChar(Builder.NULL_CHARACTER)
        .setIgnoreLeadingWhiteSpace(true)
        .setStrictQuotes(false);
  }

  /**
   * Creates a new instance of ${@link Builder} from the current instance.
   *
   * @return @return {@link Builder}.
   */
  public abstract Builder toBuilder();

  /** Constructs {@code CsvParser} using a comma as a default separator. */
  @AutoValue.Builder
  public abstract static class Builder {
    private static final char NULL_CHARACTER = '\0';
    private static final char SEPARATOR = ',';
    private static final char QUOTE_CHAR = '"';
    private static final char ESCAPE = '\\';

    public abstract Builder setEscape(char escape);

    public abstract Builder setSeparator(char separator);

    public abstract Builder setQuoteChar(char quotChar);

    public abstract Builder setNullChar(char nullChar);

    public abstract Builder setIgnoreLeadingWhiteSpace(boolean ignoreLeadingWhiteSpace);

    public abstract Builder setStrictQuotes(boolean strictQuotes);

    abstract CsvParser autoBuild();

    public final CsvParser build() {
      CsvParser csvParser = autoBuild();
      checkState(
          !anyCharactersAreTheSame(csvParser),
          "The separator, quote, and escape characters must be different.");
      checkState(
          !(csvParser.separator() == NULL_CHARACTER), "The separator character must be defined.");
      return csvParser;
    }

    private boolean anyCharactersAreTheSame(CsvParser csvParser) {
      return isSameCharacter(csvParser.nullChar(), csvParser.separator(), csvParser.quoteChar())
          || isSameCharacter(csvParser.nullChar(), csvParser.separator(), csvParser.escape())
          || isSameCharacter(csvParser.nullChar(), csvParser.quoteChar(), csvParser.escape());
    }

    private boolean isSameCharacter(char nullChar, char c1, char c2) {
      return c1 != nullChar && c1 == c2;
    }
  }
}
