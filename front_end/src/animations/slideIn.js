import { defaultTransition } from './variants.js';

export const slideIn = {
  hidden: {
    opacity: 0,
    x: -16,
  },
  visible: {
    opacity: 1,
    x: 0,
    transition: defaultTransition,
  },
};
