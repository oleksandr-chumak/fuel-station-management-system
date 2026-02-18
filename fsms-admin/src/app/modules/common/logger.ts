import { Injectable, isDevMode } from '@angular/core';

@Injectable({ providedIn: 'root' })
export class LoggerService {
    log(...args: any[]) {
        if (isDevMode()) {
            console.log(...args);
        }
    }

    warn(...args: any[]) {
        if (isDevMode()) {
            console.warn(...args);
        }
    }

    error(...args: any[]) {
        console.error(...args);
    }
}