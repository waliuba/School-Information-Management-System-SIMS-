import { lazy, Suspense } from 'react';
import { Navigate, Route, Routes } from 'react-router-dom';
import Loading from '../components/common/Loading.jsx';
import ProtectedRoute from './ProtectedRoute.jsx';
import RoleRoute from './RoleRoute.jsx';
import {
  getAttendance,
  getClasses,
  getCourses,
  getDepartments,
  getEnrollments,
  getPerformanceRows,
  getResults,
  getRoles,
  getTeachers,
  getUnits,
  getUsers,
} from '../services/api/resourcesApi.js';
import {
  attendanceColumns,
  courseColumns,
  departmentColumns,
  enrollmentColumns,
  examinationColumns,
  performanceColumns,
  teacherColumns,
} from '../pages/shared/resourceColumns.js';

const MainLayout = lazy(() => import('../components/layout/MainLayout.jsx'));
const Login = lazy(() => import('../pages/auth/Login.jsx'));
const Unauthorized = lazy(() => import('../pages/auth/Unauthorized.jsx'));
const Dashboard = lazy(() => import('../pages/dashboard/Dashboard.jsx'));
const Onboarding = lazy(() => import('../pages/onboarding/Onboarding.jsx'));
const LandingPage = lazy(() => import('../pages/landing/LandingPage.jsx'));
const ResourcePage = lazy(() => import('../pages/shared/ResourcePage.jsx'));
const StudentsPage = lazy(() => import('../pages/shared/StudentsPage.jsx'));
const PlaceholderPage = lazy(() => import('../pages/shared/PlaceholderPage.jsx'));

export default function AppRoutes() {
  return (
    <Suspense fallback={<Loading />}>
      <Routes>
        <Route path="/" element={<LandingPage />} />
        <Route path="/login" element={<Login />} />
        <Route path="/forgot-password" element={<PlaceholderPage title="Forgot password" resourcePath="/auth/forgot-password" />} />
        <Route path="/signup" element={<PlaceholderPage title="Create an account" resourcePath="/auth/register" />} />
        <Route path="/unauthorized" element={<Unauthorized />} />

        <Route element={<ProtectedRoute />}>
          <Route path="/onboarding" element={<Onboarding />} />
          <Route element={<MainLayout />}>
            <Route path="/app" element={<Navigate to="/dashboard" replace />} />
            <Route path="/dashboard" element={<Dashboard />} />
            <Route path="/students" element={<StudentsPage />} />
            <Route path="/teachers" element={<ResourcePage title="Teachers" description="Teacher records returned by the backend." request={getTeachers} columns={teacherColumns} />} />
            <Route path="/classes" element={<ResourcePage title="Classes" description="Classes returned by the backend." request={getClasses} />} />
            <Route path="/departments" element={<ResourcePage title="Departments" description="Department records returned by the backend." request={getDepartments} columns={departmentColumns} />} />
            <Route path="/courses" element={<ResourcePage title="Courses" description="Course records returned by the backend." request={getCourses} columns={courseColumns} />} />
            <Route path="/subjects" element={<ResourcePage title="Subjects" description="Academic units returned by the backend." request={getUnits} />} />
            <Route path="/enrollments" element={<ResourcePage title="Enrollments" description="Enrollment records returned by the backend." request={getEnrollments} columns={enrollmentColumns} />} />
            <Route path="/attendance" element={<ResourcePage title="Attendance" description="Latest attendance record for each student." request={getAttendance} columns={attendanceColumns} />} />
            <Route path="/results" element={<ResourcePage title="Examinations" description="Examination results returned by the backend." request={getResults} columns={examinationColumns} />} />
            <Route path="/performance" element={<ResourcePage title="Performance" description="Student performance records returned by the backend." request={getPerformanceRows} columns={performanceColumns} />} />
            <Route path="/settings" element={<PlaceholderPage title="System settings" resourcePath="/settings" />} />
            <Route element={<RoleRoute allowedRoles={['ADMIN']} />}>
              <Route path="/users" element={<ResourcePage title="Users" description="Users returned by the backend." request={getUsers} />} />
              <Route path="/roles" element={<ResourcePage title="Roles" description="Roles returned by the backend." request={getRoles} />} />
            </Route>
          </Route>
        </Route>

        <Route path="*" element={<Navigate to="/dashboard" replace />} />
      </Routes>
    </Suspense>
  );
}
