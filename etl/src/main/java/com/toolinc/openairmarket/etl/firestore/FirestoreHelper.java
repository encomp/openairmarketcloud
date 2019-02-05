package com.toolinc.openairmarket.etl.firestore;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.firestore.Firestore;
import com.google.common.flogger.FluentLogger;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.cloud.FirestoreClient;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

/** Helper class to provide access to Firestore database. */
public final class FirestoreHelper {

  private static final FluentLogger logger = FluentLogger.forEnclosingClass();
  public static final String PATH =
      "/Users/edgarrico/AndroidStudioProjects/OpenAirMarketCloud/etl/";
  private static final String CREDENTIALS =
      "openairmarket-150121-firebase-adminsdk-2flfk-1db5fe164d.json";
  private static final Object mutex = new Object();
  private static Firestore FIRESTORE;

  private FirestoreHelper() {}

  public static final Firestore getFirestore() {
    Firestore firestore = FIRESTORE;
    if (firestore == null) {
      synchronized (mutex) {
        firestore = FIRESTORE;
        if (firestore == null) {
          try (InputStream serviceAccount = new FileInputStream(PATH + CREDENTIALS)) {
            GoogleCredentials credentials = GoogleCredentials.fromStream(serviceAccount);
            FirebaseOptions options =
                new FirebaseOptions.Builder().setCredentials(credentials).build();
            FirebaseApp.initializeApp(options);
            FIRESTORE = firestore = FirestoreClient.getFirestore();
          } catch (IOException exc) {
            logger.atSevere().log("Unable to initialize Firestore.");
            throw new IllegalStateException(exc);
          }
        }
      }
    }
    return firestore;
  }
}
