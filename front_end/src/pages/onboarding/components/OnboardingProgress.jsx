export default function OnboardingProgress({ total, currentIndex }) {
  return (
    <div className="onboarding-progress" aria-label="Onboarding progress">
      {Array.from({ length: total }).map((_, index) => (
        <span
          key={index}
          className={`onboarding-progress__dot${index === currentIndex ? ' onboarding-progress__dot--active' : ''}`}
        />
      ))}
    </div>
  );
}
