const KEY = 'sims_preferences';
export const DEFAULT_PREFERENCES = { theme: 'system', sidebarCollapsed: 'false', tablePageSize: '25' };
export const PAGE_SIZE_OPTIONS = [10, 25, 50, 100];

export function getPreferredPageSize() {
  const value = Number(readPreferences().tablePageSize);
  return PAGE_SIZE_OPTIONS.includes(value) ? value : 25;
}

export function readPreferences() {
  try { return { ...DEFAULT_PREFERENCES, ...JSON.parse(localStorage.getItem(KEY) || '{}') }; }
  catch { return { ...DEFAULT_PREFERENCES }; }
}

export function applyTheme(theme = 'system') {
  const dark = theme === 'dark' || (theme === 'system' && window.matchMedia?.('(prefers-color-scheme: dark)').matches);
  document.documentElement.dataset.theme = dark ? 'dark' : 'light';
}

export function savePreferences(preferences) {
  localStorage.setItem(KEY, JSON.stringify(preferences));
  applyTheme(preferences.theme);
  window.dispatchEvent(new CustomEvent('sims:preferences-changed', { detail: preferences }));
}
