const TOKEN_KEY = 'sims_auth_token';
const USER_KEY = 'sims_auth_user';

export function getStoredToken() {
  return localStorage.getItem(TOKEN_KEY);
}

export function storeToken(token) {
  if (token) {
    localStorage.setItem(TOKEN_KEY, token);
  }
}

export function clearStoredToken() {
  localStorage.removeItem(TOKEN_KEY);
}

export function getStoredUser() {
  const value = localStorage.getItem(USER_KEY);
  return value ? JSON.parse(value) : null;
}

export function storeUser(user) {
  if (user) {
    localStorage.setItem(USER_KEY, JSON.stringify(user));
  }
}

export function clearStoredUser() {
  localStorage.removeItem(USER_KEY);
}

export function clearAuthStorage() {
  clearStoredToken();
  clearStoredUser();
}
