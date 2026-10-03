import axiosClient from './axiosClient.js';

// TODO(api): connect profile updates when the backend exposes an account endpoint.
export async function updateProfile() {
  throw new Error('Profile updates are not connected yet.');
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
