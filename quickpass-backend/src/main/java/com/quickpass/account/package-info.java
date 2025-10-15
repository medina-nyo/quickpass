/**
 * Module principal de gestion des comptes utilisateurs.
 *
 * <p>Ce package regroupe les éléments liés à la gestion des comptes :
 * <ul>
 *   <li>Création et gestion des entités {@code Compte} et {@code Preference}</li>
 *   <li>Services métiers associés (préférences RGPD, statut, suppression, etc.)</li>
 *   <li>Extension vers des sous-types spécifiques (ex. {@code Boulanger})</li>
 * </ul>
 *
 * <p>Les classes de ce module sont partagées entre les différents types
 * de flux d’inscription et de connexion (Boulanger, Client, Employé...).
 */
package com.quickpass.account;
