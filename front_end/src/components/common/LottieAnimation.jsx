import { Lottie } from 'lottie-react';

export default function LottieAnimation({ animationData, className = '', loop = true, autoplay = true }) {
  return (
    <div className={`lottie-animation ${className}`.trim()} aria-hidden="true">
      <Lottie src={animationData} loop={loop} autoplay={autoplay} />
    </div>
  );
}
