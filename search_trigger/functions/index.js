// The Cloud Functions for Firebase SDK to create Cloud Functions and setup triggers.
const functions = require('firebase-functions');

// The Firebase Admin SDK to access the Firebase Realtime Database.
const admin = require('firebase-admin');
admin.initializeApp();
XMLHttpRequest = require("xmlhttprequest").XMLHttpRequest;

// Take the text parameter passed to this HTTP endpoint and insert it into the
// Realtime Database under the path /messages/:pushId/original
exports.addMessage = functions.https.onRequest((req, res) => {
  // Grab the text parameter.
  const original = req.query.text;
  // Push the new message into the Realtime Database using the Firebase Admin SDK.
  return admin.database().ref('/messages').push({original: original}).then((snapshot) => {
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

exports.modifyProductBrand = functions.firestore.document('productBrands/{productBrandId}')
    .onCreate((change, context) => {
      // Get an object with the current document value.
      // If the document does not exist, it has been deleted.
      const document = change.after.exists ? change.after.data() : null;
      // Get an object with the previous document value (for update or delete)
      const oldDocument = change.before.data();

      /* var endpoint = `https://openairmarket-150121.appspot.com/_ah/api/search/v1/create/productBrand`;
              var xhr = new XMLHttpRequest();
              xhr.open('POST', endpoint);
              xhr.onreadystatechange = function () {
                if (this.readyState === XMLHttpRequest.DONE && this.status === 200) {
                  xhr.responseText;
                }
              };
      return xhr.send(JSON.stringify(document));
      */

      return new Promise((() => {
                 var xhr = new XMLHttpRequest();
                 xhr.open('POST', 'https://openairmarket-150121.appspot.com/_ah/api/search/v1/create/productBrand');
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
                 xhr.send(JSON.stringify(document));
               }));
    });