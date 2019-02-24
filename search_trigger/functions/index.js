// The Cloud Functions for Firebase SDK to create Cloud Functions and setup triggers.
const functions = require('firebase-functions');

// The Firebase Admin SDK to access the Firebase Realtime Database.
const admin = require('firebase-admin');
admin.initializeApp();
XMLHttpRequest = require("xmlhttprequest").XMLHttpRequest;

function endPointRequest(url, data) {
  return new Promise((() => {
    var xhr = new XMLHttpRequest();
    xhr.open('POST', 'https://openairmarket-150121.appspot.com/_ah/api/search/v1/' + url);
    xhr.onload = function () {
      if (this.status === 200 && this.status < 300) {
        return xhr.response;
      } else {
        return {
          status: this.status,
          statusText: xhr.statusText
        };
      }
    };
    xhr.onerror = function () {
      return {
        status: this.status,
        statusText: xhr.statusText
      };
    };
    xhr.send(JSON.stringify(data));
  }));
}

exports.createProductBrand = functions.firestore.document('productBrands/{productBrandId}')
  .onCreate((snap, context) => {
    return endPointRequest('create/productBrand', snap.data());
  });

exports.createProductCategory = functions.firestore.document('productCategories/{productCategoryId}')
  .onCreate((snap, context) => {
    return endPointRequest('create/productCategory', snap.data());
  });

exports.createProductManufacturer = functions.firestore.document('productManufacturers/{productManufacturerId}')
  .onCreate((snap, context) => {
    return endPointRequest('create/productManufacturer', snap.data());
  });

exports.createProductMeasureUnit = functions.firestore.document('productMeasureUnits/{productMeasureUnitId}')
  .onCreate((snap, context) => {
    return endPointRequest('create/productMeasureUnit', snap.data());
  });

exports.createProduct = functions.firestore.document('products/{productId}')
  .onCreate((snap, context) => {
    return endPointRequest('create/product', snap.data());
  });  