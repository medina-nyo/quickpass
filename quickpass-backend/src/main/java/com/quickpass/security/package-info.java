/**
 * Configuration de la sécurité de l’application QuickPass.
 *
 * <p>Ce package contient la configuration Spring Security utilisée
 * pour protéger les endpoints de l’API :
 * <ul>
 *   <li>Configuration stateless via {@code SecurityConfig}</li>
 *   <li>Autorisation publique des endpoints d’inscription</li>
 *   <li>Utilisation de l’encodeur Argon2id pour le hashing des mots de passe</li>
 * </ul>
 *
 * <p>Les routes critiques (ex. back-office) nécessiteront un renforcement via JWT ultérieurement.
 */
package com.quickpass.security;
