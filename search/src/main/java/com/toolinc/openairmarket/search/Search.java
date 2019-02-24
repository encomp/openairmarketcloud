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
import com.toolinc.openairmarket.pos.persistence.model.product.Product;
import com.toolinc.openairmarket.pos.persistence.model.product.ProductBrand;
import com.toolinc.openairmarket.pos.persistence.model.product.ProductCategory;
import com.toolinc.openairmarket.pos.persistence.model.product.ProductManufacturer;
import com.toolinc.openairmarket.pos.persistence.model.product.ProductMeasureUnit;

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
  private static final String REFERENCE_ID = "referenceId";
  private static final String NAME = "name";
  private static final String ACTIVE = "active";
  private static final String PRODUCT_BRANDS = "productBrands";
  private static final String PRODUCT_CATEGORIES = "productCategories";
  private static final String PRODUCT_MANUFACTURERS = "productManufacturers";
  private static final String PRODUCT_MEASURE_UNITS = "productMeasureUnits";
  private static final String PRODUCTS = "products";

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

  /** Creates a new {@link ProductBrand} on a {@link Index}. */
  @ApiMethod(name = "create_product_brand", path = "create/productBrand")
  public void createProductBrand(ProductBrand productBrand) {
    Document.Builder builder =
        addReferenceField(
            Document.newBuilder().setId(productBrand.id()), productBrand.getReferenceId());
    addNameField(builder, productBrand.getName())
        .addField(
            Field.newBuilder()
                .setName("productManufacturer")
                .setText(productBrand.getProductManufacturer())
                .build());
    Document document = addActiveField(builder, productBrand.getActive()).build();
    storeDocument(PRODUCT_BRANDS, document);
  }

  /** Stores a new {@link ProductCategory} on an {@link Index}. */
  @ApiMethod(name = "create_product_category", path = "create/productCategory")
  public void createProductCategory(ProductCategory productCategory) {
    Document.Builder builder =
        addReferenceField(
            Document.newBuilder().setId(productCategory.id()), productCategory.getReferenceId());
    addNameField(builder, productCategory.getName());
    Document document = addActiveField(builder, productCategory.getActive()).build();
    storeDocument(PRODUCT_CATEGORIES, document);
  }

  /** Stores a new {@link ProductManufacturer} on an {@link Index}. */
  @ApiMethod(name = "create_product_manufacturer", path = "create/productManufacturer")
  public void createProductManufacturer(ProductManufacturer productManufacturer) {
    Document.Builder builder =
        addReferenceField(
            Document.newBuilder().setId(productManufacturer.id()),
            productManufacturer.getReferenceId());
    addNameField(builder, productManufacturer.getName());
    Document document = addActiveField(builder, productManufacturer.getActive()).build();
    storeDocument(PRODUCT_MANUFACTURERS, document);
  }

  /** Stores a new {@link ProductMeasureUnit} on an {@link Index}. */
  @ApiMethod(name = "create_product_measure_unit", path = "create/productMeasureUnit")
  public void createProductMeasureUnit(ProductMeasureUnit productMeasureUnit) {
    Document.Builder builder =
        addReferenceField(
            Document.newBuilder().setId(productMeasureUnit.id()),
            productMeasureUnit.getReferenceId());
    addNameField(builder, productMeasureUnit.getName())
        .addField(
            Field.newBuilder()
                .setName("countable")
                .setText(productMeasureUnit.getCountable().toString())
                .build());
    Document document = addActiveField(builder, productMeasureUnit.getActive()).build();
    storeDocument(PRODUCT_MEASURE_UNITS, document);
  }

  /** Stores a new {@link Product} on an {@link Index}. */
  @ApiMethod(name = "create_product", path = "create/product")
  public void createProductt(Product product) {
    Document.Builder builder =
        addReferenceField(Document.newBuilder().setId(product.id()), product.getReferenceId());
    addNameField(builder, product.getName())
        .addField(
            Field.newBuilder()
                .setName("productType")
                .setText(product.getProductType().name())
                .build())
        .addField(
            Field.newBuilder().setName("productBrand").setText(product.getProductBrand()).build())
        .addField(
            Field.newBuilder()
                .setName("productCategory")
                .setText(product.getProductCategory())
                .build())
        .addField(
            Field.newBuilder()
                .setName("productMeasureUnit")
                .setText(product.getProductMeasureUnit())
                .build())
        .addField(
            Field.newBuilder()
                .setName("productSalePrice")
                .setText(product.getProductSalePrice().getPrice())
                .build())
        .addField(
            Field.newBuilder()
                .setName("productPurchasePrice")
                .setText(product.getProductPurchasePrice().getPrice())
                .build());
    Document document = addActiveField(builder, product.getActive()).build();
    storeDocument(PRODUCTS, document);
  }

  private static final Document.Builder addReferenceField(Document.Builder builder, String value) {
    return builder.addField(Field.newBuilder().setName(REFERENCE_ID).setText(value).build());
  }

  private static final Document.Builder addNameField(Document.Builder builder, String value) {
    return builder.addField(Field.newBuilder().setName(NAME).setText(value).build());
  }

  private static final Document.Builder addActiveField(Document.Builder builder, Boolean value) {
    return builder.addField(Field.newBuilder().setName(ACTIVE).setText(value.toString()).build());
  }

  private static final void storeDocument(String indexName, Document document) {
    IndexSpec indexSpec = IndexSpec.newBuilder().setName(indexName).build();
    Index index = SearchServiceFactory.getSearchService().getIndex(indexSpec);
    index.put(document);
  }
}
