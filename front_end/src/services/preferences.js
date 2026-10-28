const KEY = 'sims_preferences';
export const DEFAULT_PREFERENCES = { theme: 'light', sidebarCollapsed: 'false', tablePageSize: '25' };
export const PAGE_SIZE_OPTIONS = [10, 25, 50, 100];

export function getPreferredPageSize() {
  const value = Number(readPreferences().tablePageSize);
  return PAGE_SIZE_OPTIONS.includes(value) ? value : 25;
}

export function readPreferences() {
  try { return { ...DEFAULT_PREFERENCES, ...JSON.parse(localStorage.getItem(KEY) || '{}'), theme: 'light' }; }
  catch { return { ...DEFAULT_PREFERENCES }; }
}

export function applyTheme() {
  document.documentElement.dataset.theme = 'light';
  document.documentElement.style.colorScheme = 'light';
}

export function savePreferences(preferences) {
  const lightPreferences = { ...preferences, theme: 'light' };
  localStorage.setItem(KEY, JSON.stringify(lightPreferences));
  applyTheme();
  window.dispatchEvent(new CustomEvent('sims:preferences-changed', { detail: lightPreferences }));
}
