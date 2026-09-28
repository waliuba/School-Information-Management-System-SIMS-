import axiosClient from './axiosClient.js';

function unwrapList(response) {
  const payload = response.data;
  if (Array.isArray(payload)) {
    return payload;
  }
  return Array.isArray(payload?.data) ? payload.data : [];
}

async function getList(path, params) {
  try {
    const response = await axiosClient.get(path, { params });
    return unwrapList(response);
  } catch (error) {
    if (error.status === 404) {
      return [];
    }
    throw error;
  }
}

export function getStudents(params) {
  return getList('/students', params);
}

export function getTeachers(params) {
  return getList('/teachers', params);
}

export function getClasses(params) {
  return getList('/classes', params);
}

export function getDepartments(params) {
  return getList('/departments', params);
}

export function getCourses(params) {
  return getList('/courses', params);
}

export function getUnits(params) {
  return getList('/units', params);
}

export function getEnrollments(params) {
  return getList('/enrollments', params);
}

export function getAttendance(params) {
  return getList('/attendance', params);
}

export function getResults(params) {
  return getList('/results', params);
}

export async function getPerformanceRows() {
  const response = await axiosClient.get('/dashboard/performance');
  const rows = response.data?.rows;

  if (!Array.isArray(rows)) {
    throw new Error('The backend performance response did not contain a rows array.');
  }

  return rows;
}

export function getUsers(params) {
  return getList('/users', params);
}

export function getRoles(params) {
  return getList('/roles', params);
}
