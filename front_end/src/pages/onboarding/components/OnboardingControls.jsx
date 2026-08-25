import Button from '../../../components/common/Button.jsx';

export default function OnboardingControls({ canGoBack, isLastSlide, onBack, onNext, onSkip }) {
  return (
    <div className="onboarding-controls">
      <Button type="button" variant="secondary" onClick={onBack} disabled={!canGoBack}>
        Back
      </Button>
      <div>
        <Button type="button" variant="secondary" onClick={onSkip}>
          Skip
        </Button>
        <Button type="button" onClick={onNext}>
          {isLastSlide ? 'Go to login' : 'Next'}
        </Button>
      </div>
    </div>
  );
}
