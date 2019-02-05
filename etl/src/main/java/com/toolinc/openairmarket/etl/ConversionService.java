package com.toolinc.openairmarket.etl;

/**
 * Specifies the contract for the transformation service which transforms data based on a set of SQL
 * scripts.
 */
public interface ConversionService {

  /**
   * Extract the information by using a specified SQL script the information extracted will be
   * stored in a file.
   *
   * @param fileNames the set of script that will be executed to perform the transformation.
   * @return true if the transformation was successful otherwise will return false.
   */
  boolean transform(String... fileNames);
}
