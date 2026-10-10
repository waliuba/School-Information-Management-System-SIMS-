const ACTIVITY_STORAGE_KEY = 'sims_recent_activity';

export function recordActivity(action, resource) {
  const activity = { action, resource, completedAt: new Date().toISOString() };
  try {
    window.localStorage.setItem(ACTIVITY_STORAGE_KEY, JSON.stringify(activity));
  } catch {
    // Activity display is best effort when browser storage is unavailable.
  }
}

export function getTodaysActivity() {
  try {
    const activity = JSON.parse(window.localStorage.getItem(ACTIVITY_STORAGE_KEY) || 'null');
    if (!activity?.completedAt || new Date(activity.completedAt).toDateString() !== new Date().toDateString()) return null;
    return activity;
  } catch {
    return null;
  }
}
