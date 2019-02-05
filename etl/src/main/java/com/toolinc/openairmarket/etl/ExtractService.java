package com.toolinc.openairmarket.etl;

/**
 * Specifies the contract for the extraction service which extracts information from a database
 * given a SQL script.
 */
public interface ExtractService {

  /**
   * Extract the information by using a specified SQL script the information extracted will be
   * stored in a file.
   *
   * @param sqlScript the SQL script that will be used to extract the information.
   * @param outputPath the path in which the file will be stored.
   * @param fileName the name that will be used to create a file in which extracted information will
   *     be stored.
   * @return true if the extraction process finished successfully otherwise will return false.
   */
  boolean extract(String sqlScript, String outputPath, String fileName);
}
