import loadingAnimation from '../../assets/lottie/loading.json';
import LottieAnimation from './LottieAnimation.jsx';

export default function LoadingScreen({ message = 'Loading SIMS...' }) {
  return (
    <main className="loading-screen" role="status" aria-live="polite">
      <LottieAnimation animationData={loadingAnimation} className="loading-screen__animation" />
      <p>{message}</p>
    </main>
  );
}
