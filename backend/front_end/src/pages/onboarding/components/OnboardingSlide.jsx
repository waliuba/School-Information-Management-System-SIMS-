import LottieAnimation from '../../../components/common/LottieAnimation.jsx';

export default function OnboardingSlide({ slide }) {
  return (
    <div className="onboarding-slide">
      <LottieAnimation animationData={slide.animationData} className="onboarding-slide__animation" />
      <h1>{slide.title}</h1>
      <p>{slide.description}</p>
    </div>
  );
}
