import { useCallback, useEffect, useState } from 'react';

export function useApi(requestFn, options = {}) {
  const { immediate = true, refetchOnWindowFocus = false } = options;
  const [data, setData] = useState(null);
  const [error, setError] = useState(null);
  const [isLoading, setIsLoading] = useState(immediate);

  const execute = useCallback(
    async (...args) => {
      setIsLoading(true);
      setError(null);

      try {
        const result = await requestFn(...args);
        setData(result);
        return result;
      } catch (apiError) {
        setError(apiError);
        throw apiError;
      } finally {
        setIsLoading(false);
      }
    },
    [requestFn]
  );

  useEffect(() => {
    if (immediate) {
      execute();
    }
  }, [execute, immediate]);

  useEffect(() => {
    if (!refetchOnWindowFocus) {
      return undefined;
    }

    const handleWindowFocus = async () => {
      try {
        await execute();
      } catch {
        // execute stores the request error for the caller to render.
      }
    };

    window.addEventListener('focus', handleWindowFocus);
    return () => window.removeEventListener('focus', handleWindowFocus);
  }, [execute, refetchOnWindowFocus]);

  return { data, error, isLoading, execute, setData };
}
