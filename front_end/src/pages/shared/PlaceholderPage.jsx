import PageContainer from '../../components/layout/PageContainer.jsx';

export default function PlaceholderPage({ title, resourcePath }) {
  return (
    <PageContainer title={title} description={`Phase implementation will connect this page to ${resourcePath}.`}>
      <div className="empty-state">
        <strong>{title} module placeholder</strong>
        <p>
          This route is ready for the next phase. API calls should live in a matching service file,
          then this page should display backend data and request states.
        </p>
      </div>
    </PageContainer>
  );
}
