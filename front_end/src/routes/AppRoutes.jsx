import { Navigate, Route, Routes } from 'react-router-dom';
import MainLayout from '../components/layout/MainLayout.jsx';
import Login from '../pages/auth/Login.jsx';
import Unauthorized from '../pages/auth/Unauthorized.jsx';
import Dashboard from '../pages/dashboard/Dashboard.jsx';
import Onboarding from '../pages/onboarding/Onboarding.jsx';
import PlaceholderPage from '../pages/shared/PlaceholderPage.jsx';
import ProtectedRoute from './ProtectedRoute.jsx';

export default function AppRoutes() {
  return (
    <Routes>
      <Route path="/login" element={<Login />} />
      <Route path="/onboarding" element={<Onboarding />} />
      <Route path="/unauthorized" element={<Unauthorized />} />

      <Route element={<ProtectedRoute />}>
        <Route element={<MainLayout />}>
          <Route index element={<Navigate to="/dashboard" replace />} />
          <Route path="/dashboard" element={<Dashboard />} />
          <Route path="/students" element={<PlaceholderPage title="Students" resourcePath="/api/students" />} />
          <Route path="/teachers" element={<PlaceholderPage title="Teachers" resourcePath="/api/teachers" />} />
          <Route path="/classes" element={<PlaceholderPage title="Classes" resourcePath="/api/classes" />} />
          <Route path="/departments" element={<PlaceholderPage title="Departments" resourcePath="/api/departments" />} />
          <Route path="/subjects" element={<PlaceholderPage title="Subjects" resourcePath="/api/subjects" />} />
          <Route path="/enrollments" element={<PlaceholderPage title="Enrollments" resourcePath="/api/enrollments" />} />
          <Route path="/attendance" element={<PlaceholderPage title="Attendance" resourcePath="/api/attendance" />} />
          <Route path="/results" element={<PlaceholderPage title="Exams & Results" resourcePath="/api/results" />} />
          <Route path="/users" element={<PlaceholderPage title="Users" resourcePath="/api/users" />} />
          <Route path="/roles" element={<PlaceholderPage title="Roles" resourcePath="/api/roles" />} />
        </Route>
      </Route>

      <Route path="*" element={<Navigate to="/dashboard" replace />} />
    </Routes>
  );
}
