import { useCallback, useEffect, useState } from 'react';

export function useApi(requestFn, options = {}) {
  const { immediate = true } = options;
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

  return { data, error, isLoading, execute, setData };
}
