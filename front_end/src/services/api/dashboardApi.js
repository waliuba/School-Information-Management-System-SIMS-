import axiosClient from './axiosClient.js';

export async function getDashboardSummary() {
  const response = await axiosClient.get('/dashboard/summary');
  return response.data;
}

export async function getDashboardPerformance() {
  const response = await axiosClient.get('/dashboard/performance');
  return response.data;
}

function unwrapResource(response) {
  return response.data?.data ?? response.data ?? [];
}

async function getResource(path) {
  const response = await axiosClient.get(path);
  return unwrapResource(response);
}

async function getOptionalResource(path) {
  try {
    return await getResource(path);
  } catch (error) {
    if (error.status === 404) {
      return null;
    }
    throw error;
  }
}

export async function getAdminDashboardData() {
  const [summary, performance, students, teachers, departments, enrollments, attendance, examinations] =
    await Promise.all([
      getDashboardSummary(),
      getDashboardPerformance(),
      getOptionalResource('/students'),
      getOptionalResource('/teachers'),
      getOptionalResource('/departments'),
      getOptionalResource('/enrollments'),
      getOptionalResource('/attendance'),
      getOptionalResource('/results'),
    ]);

  return {
    summary,
    performance,
    students,
    teachers,
    departments,
    enrollments,
    attendance,
    examinations,
  };
}
