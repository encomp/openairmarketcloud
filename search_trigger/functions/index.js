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

// Take the text parameter passed to this HTTP endpoint and insert it into the
// Realtime Database under the path /messages/:pushId/original
exports.addMessage = functions.https.onRequest((req, res) => {
  // Grab the text parameter.
  const original = req.query.text;
  // Push the new message into the Realtime Database using the Firebase Admin SDK.
  return admin.database().ref('/messages').push({ original: original }).then((snapshot) => {
    // Redirect with 303 SEE OTHER to the URL of the pushed object in the Firebase console.
    return res.redirect(303, snapshot.ref.toString());
  });
});

// Listens for new messages added to /messages/:pushId/original and creates an
// uppercase version of the message to /messages/:pushId/uppercase
exports.makeUppercase = functions.database.ref('/messages/{pushId}/original')
  .onCreate((snapshot, context) => {
    // Grab the current value of what was written to the Realtime Database.
    const original = snapshot.val();
    console.log('Uppercasing', context.params.pushId, original);
    const uppercase = original.toUpperCase();
    // You must return a Promise when performing asynchronous tasks inside a Functions such as
    // writing to the Firebase Realtime Database.
    // Setting an "uppercase" sibling in the Realtime Database returns a Promise.
    return snapshot.ref.parent.child('uppercase').set(uppercase);
  });

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