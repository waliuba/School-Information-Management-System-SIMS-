import ResourcePage from './ResourcePage.jsx';
import { getStudents } from '../../services/api/resourcesApi.js';
import { studentColumns } from './resourceColumns.js';

export default function StudentsPage() {
  return (
    <ResourcePage
      title="Students"
      description="Student records returned by the backend."
      request={getStudents}
      columns={studentColumns}
    />
  );
}
