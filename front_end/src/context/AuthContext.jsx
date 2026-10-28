import { createContext, useCallback, useEffect, useMemo, useState } from 'react';
import { getCurrentUser, login as loginRequest, logout as logoutRequest } from '../services/api/authApi.js';
import { getPreferences } from '../services/api/accountApi.js';
import { savePreferences } from '../services/preferences.js';
import {
  clearAuthStorage,
  getStoredToken,
  getStoredUser,
  storeToken,
  storeUser,
} from '../services/storage/authStorage.js';

export const AuthContext = createContext(null);

function normalizeRole(role) {
  return role === 'SUPER_ADMIN' ? 'ADMIN' : role;
}

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
        try {
          const preferences = await getPreferences();
          if (isMounted) savePreferences(preferences);
        } catch {
          // Keep the last-known device preferences if the preference endpoint is unavailable.
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
    setIsAuthLoading(true);
    const data = await loginRequest(credentials);
    const nextToken = data.token || data.accessToken;
    const nextUser = {
      ...(data.user || {
        userId: data.userId,
        username: data.username,
        email: data.email,
      }),
      role: normalizeRole(data.user?.role ?? data.role),
    };

    storeToken(nextToken);
    storeUser(nextUser);
    setToken(nextToken);
    setUser(nextUser);

    return data;
  }, []);

  const logout = useCallback(async () => {
    try {
      await logoutRequest();
    } catch {
      // Local sign-out must still complete when the stateless endpoint is unreachable.
    } finally {
      clearAuthStorage();
      setToken(null);
      setUser(null);
    }
  }, []);

  const updateCurrentUser = useCallback((nextUser) => {
    storeUser(nextUser);
    setUser(nextUser);
  }, []);

  const value = useMemo(
    () => ({
      user,
      token,
      isAuthenticated: Boolean(token),
      isAuthLoading,
      login,
      logout,
      updateCurrentUser,
    }),
    [user, token, isAuthLoading, login, logout, updateCurrentUser]
  );

  return <AuthContext.Provider value={value}>{children}</AuthContext.Provider>;
}
