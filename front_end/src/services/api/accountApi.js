import axiosClient from './axiosClient.js';

export async function updateProfile(profile) {
  const response = await axiosClient.put('/auth/me', {
    email: profile.email,
  });
  return response.data;
}

export async function changePassword(passwords) {
  const response = await axiosClient.put('/auth/change-password', {
    currentPassword: passwords.currentPassword,
    newPassword: passwords.newPassword,
  });
  return response.data;
}

export async function getNotifications() {
  throw new Error('Notifications are not connected yet.');
}

export async function getPreferences() {
  const response = await axiosClient.get('/settings/me/preferences');
  return response.data;
}

export async function updatePreferences(preferences) {
  const response = await axiosClient.put('/settings/me/preferences', preferences);
  return response.data;
}
