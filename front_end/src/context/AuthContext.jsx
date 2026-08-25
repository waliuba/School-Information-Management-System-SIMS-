import { createContext, useCallback, useEffect, useMemo, useState } from 'react';
import { getCurrentUser, login as loginRequest, logout as logoutRequest } from '../services/api/authApi.js';
import {
  clearAuthStorage,
  getStoredToken,
  getStoredUser,
  storeToken,
  storeUser,
} from '../services/storage/authStorage.js';

export const AuthContext = createContext(null);

export function AuthProvider({ children }) {
  const [user, setUser] = useState(() => getStoredUser());
  const [token, setToken] = useState(() => getStoredToken());
  const [isAuthLoading, setIsAuthLoading] = useState(true);

  useEffect(() => {
    let isMounted = true;

    async function loadCurrentUser() {
      if (!token) {
        setIsAuthLoading(false);
        return;
      }

      try {
        const currentUser = await getCurrentUser();

        if (isMounted) {
          setUser(currentUser);
          storeUser(currentUser);
        }
      } catch {
        if (isMounted) {
          setUser(null);
          setToken(null);
          clearAuthStorage();
        }
      } finally {
        if (isMounted) {
          setIsAuthLoading(false);
        }
      }
    }

    loadCurrentUser();

    return () => {
      isMounted = false;
    };
  }, [token]);

  const login = useCallback(async (credentials) => {
    const data = await loginRequest(credentials);
    const nextToken = data.token || data.accessToken;
    const nextUser = data.user;

    storeToken(nextToken);
    storeUser(nextUser);
    setToken(nextToken);
    setUser(nextUser);

    return data;
  }, []);

  const logout = useCallback(async () => {
    try {
      await logoutRequest();
    } finally {
      clearAuthStorage();
      setToken(null);
      setUser(null);
    }
  }, []);

  const value = useMemo(
    () => ({
      user,
      token,
      isAuthenticated: Boolean(token),
      isAuthLoading,
      login,
      logout,
    }),
    [user, token, isAuthLoading, login, logout]
  );

  return <AuthContext.Provider value={value}>{children}</AuthContext.Provider>;
}
