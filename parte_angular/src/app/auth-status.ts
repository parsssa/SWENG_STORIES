// auth-status.ts

import { BehaviorSubject } from 'rxjs';

export const authStatus = new BehaviorSubject<boolean>(false);
