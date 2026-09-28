import { Link } from 'react-router-dom';
import PageContainer from '../../components/layout/PageContainer.jsx';

export default function Unauthorized() {
  return (
    <PageContainer title="Unauthorized" description="The backend must still verify permissions for every request.">
      <div className="empty-state">
        <p>Your current role is not allowed to view this frontend route.</p>
        <Link className="button button--primary" to="/dashboard">
          Back to dashboard
        </Link>
      </div>
    </PageContainer>
  );
}
