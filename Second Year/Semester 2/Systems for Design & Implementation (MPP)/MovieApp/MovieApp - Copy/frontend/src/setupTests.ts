// src/setupTests.ts

// 1) Import Jest DOM so we can use `toBeInTheDocument()` and similar matchers.
import "@testing-library/jest-dom";

// 2) If you want to use Vitest's `expect` globally (optional if you have `globals: true` in config):
//    import { expect } from "vitest";
//    globalThis.expect = expect;
// 
// Usually, if `globals: true` is set in `vite.config.ts`, Vitest will automatically inject `expect`.
