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
