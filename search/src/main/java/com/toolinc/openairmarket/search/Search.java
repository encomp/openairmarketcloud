package com.toolinc.openairmarket.search;

import com.google.api.server.spi.auth.EspAuthenticator;
import com.google.api.server.spi.auth.common.User;
import com.google.api.server.spi.config.AnnotationBoolean;
import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiIssuer;
import com.google.api.server.spi.config.ApiIssuerAudience;
import com.google.api.server.spi.config.ApiMethod;
import com.google.api.server.spi.config.ApiNamespace;
import com.google.api.server.spi.config.Named;
import com.google.api.server.spi.config.Nullable;
import com.google.api.server.spi.response.UnauthorizedException;
import com.google.appengine.api.search.Document;
import com.google.appengine.api.search.Field;
import com.google.appengine.api.search.Index;
import com.google.appengine.api.search.IndexSpec;
import com.google.appengine.api.search.SearchServiceFactory;
import com.toolinc.openairmarket.pos.persistence.model.product.ProductBrand;

/** The Search API which Endpoints will be exposing. */
@Api(
    name = "search",
    version = "v1",
    namespace =
        @ApiNamespace(
            ownerDomain = "search.openairmarket.toolinc.com",
            ownerName = "search.openairmarket.toolinc.com",
            packagePath = ""),
    issuers = {
      @ApiIssuer(
          name = "firebase",
          issuer = "https://securetoken.google.com/openairmarket-150121",
          jwksUri =
              "https://www.googleapis.com/robot/v1/metadata/x509/securetoken@system.gserviceaccount.com")
    })
public class Search {
  /**
   * Echoes the received message back. If n is a non-negative integer, the message is copied that
   * many times in the returned message.
   *
   * <p>Note that name is specified and will override the default name of "{class name}.{method
   * name}". For example, the default is "echo.echo".
   *
   * <p>Note that httpMethod is not specified. This will default to a reasonable HTTP method
   * depending on the API method name. In this case, the HTTP method will default to POST.
   */
  @ApiMethod(name = "echo")
  public Message echo(Message message, @Named("n") @Nullable Integer n) {
    return doEcho(message, n);
  }

  /**
   * Echoes the received message back. If n is a non-negative integer, the message is copied that
   * many times in the returned message.
   *
   * <p>Note that name is specified and will override the default name of "{class name}.{method
   * name}". For example, the default is "echo.echo".
   *
   * <p>Note that httpMethod is not specified. This will default to a reasonable HTTP method
   * depending on the API method name. In this case, the HTTP method will default to POST.
   */
  @ApiMethod(name = "echo_path_parameter", path = "echo/{n}")
  public Message echoPathParameter(Message message, @Named("n") int n) {
    return doEcho(message, n);
  }

  /**
   * Echoes the received message back. If n is a non-negative integer, the message is copied that
   * many times in the returned message.
   *
   * <p>Note that name is specified and will override the default name of "{class name}.{method
   * name}". For example, the default is "echo.echo".
   *
   * <p>Note that httpMethod is not specified. This will default to a reasonable HTTP method
   * depending on the API method name. In this case, the HTTP method will default to POST.
   */
  @ApiMethod(name = "echo_api_key", path = "echo_api_key", apiKeyRequired = AnnotationBoolean.TRUE)
  public Message echoApiKey(Message message, @Named("n") @Nullable Integer n) {
    return doEcho(message, n);
  }

  private Message doEcho(Message message, Integer n) {
    if (n != null && n >= 0) {
      StringBuilder sb = new StringBuilder();
      for (int i = 0; i < n; i++) {
        if (i > 0) {
          sb.append(" ");
        }
        sb.append(message.getMessage());
      }
      message.setMessage(sb.toString());
    }
    return message;
  }

  /**
   * Gets the authenticated user's email. If the user is not authenticated, this will return an HTTP
   * 401.
   *
   * <p>Note that name is not specified. This will default to "{class name}.{method name}". For
   * example, the default is "echo.getUserEmail".
   *
   * <p>Note that httpMethod is not required here. Without httpMethod, this will default to GET due
   * to the API method name. httpMethod is added here for example purposes.
   */
  @ApiMethod(
      httpMethod = ApiMethod.HttpMethod.GET,
      authenticators = {EspAuthenticator.class},
      audiences = {"YOUR_OAUTH_CLIENT_ID"},
      clientIds = {"YOUR_OAUTH_CLIENT_ID"})
  public Email getUserEmail(User user) throws UnauthorizedException {
    if (user == null) {
      throw new UnauthorizedException("Invalid credentials");
    }

    Email response = new Email();
    response.setEmail(user.getEmail());
    return response;
  }

  /**
   * Gets the authenticated user's email. If the user is not authenticated, this will return an HTTP
   * 401.
   *
   * <p>Note that name is not specified. This will default to "{class name}.{method name}". For
   * example, the default is "echo.getUserEmail".
   *
   * <p>Note that httpMethod is not required here. Without httpMethod, this will default to GET due
   * to the API method name. httpMethod is added here for example purposes.
   */
  @ApiMethod(
      path = "firebase_user",
      httpMethod = ApiMethod.HttpMethod.GET,
      authenticators = {EspAuthenticator.class},
      issuerAudiences = {
        @ApiIssuerAudience(
            name = "firebase",
            audiences = {"openairmarket-150121"})
      })
  public Email getUserEmailFirebase(User user) throws UnauthorizedException {
    if (user == null) {
      throw new UnauthorizedException("Invalid credentials");
    }

    Email response = new Email();
    response.setEmail(user.getEmail());
    return response;
  }

  @ApiMethod(name = "drop_index", path = "drop/index/{index}")
  public void dropIndex(@Named("index") String index) {
    IndexSpec indexSpec = IndexSpec.newBuilder().setName(index).build();
    SearchServiceFactory.getSearchService().getIndex(indexSpec).deleteSchema();
  }

  /**
   * Creates a new {@link ProductBrand} on a {@link Index}.
   *
   * @param productBrand the brand to be stored.
   */
  @ApiMethod(name = "create_product_brand", path = "create/productBrand")
  public void createProductBrand(ProductBrand productBrand) {
    Document document =
        Document.newBuilder()
            .setId(productBrand.id())
            .addField(
                Field.newBuilder()
                    .setName("referenceId")
                    .setText(productBrand.getReferenceId())
                    .build())
            .addField(Field.newBuilder().setName("name").setText(productBrand.getName()).build())
            .addField(
                Field.newBuilder()
                    .setName("productManufacturer")
                    .setText(productBrand.getProductManufacturer())
                    .build())
            .addField(
                Field.newBuilder()
                    .setName("active")
                    .setText(productBrand.getActive().toString())
                    .build())
            .build();
    IndexSpec indexSpec = IndexSpec.newBuilder().setName("productBrand").build();
    Index index = SearchServiceFactory.getSearchService().getIndex(indexSpec);
    index.put(document);
  }
}
