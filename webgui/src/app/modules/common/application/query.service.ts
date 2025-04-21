
import { Observable, Subscription, BehaviorSubject } from "rxjs";

export default class QueryService<T, Args extends any[]> {
    private method: (...args: Args) => Observable<T>;
    
    private dataSubject = new BehaviorSubject<T | null>(null);
    private loadingSubject = new BehaviorSubject<boolean>(false);
    private errorSubject = new BehaviorSubject<any>(null);
    
    data$: Observable<T | null> = this.dataSubject.asObservable();
    loading$: Observable<boolean> = this.loadingSubject.asObservable();
    error$: Observable<any> = this.errorSubject.asObservable();
    
    private subscription: Subscription | null = null;
    
    constructor(method: (...args: Args) => Observable<T>) {
        this.method = method;
    }
    
    executeQuery(...args: Args): void {
        if (this.subscription) {
            this.subscription.unsubscribe();
        }
        
        // Set loading state
        this.loadingSubject.next(true);
        this.errorSubject.next(null);
        
        // Execute the method with the provided arguments
        this.subscription = this.method(...args).subscribe({
            next: (result) => {
                console.log("Query result", result)
                this.dataSubject.next(result);
                this.loadingSubject.next(false);
            },
            error: (err) => {
                console.error("Error: ", err)
                this.errorSubject.next(err);
                this.loadingSubject.next(false);
                this.dataSubject.next(null);
            },
            complete: () => {
                this.loadingSubject.next(false);
            }
        });
    }
    
    get data(): T | null {
        return this.dataSubject.getValue();
    }
    
    get loading(): boolean {
        return this.loadingSubject.getValue();
    }
    
    get error(): any {
        return this.errorSubject.getValue();
    }
    
    // Method to cancel current request
    cancel(): void {
        if (this.subscription) {
            this.subscription.unsubscribe();
            this.subscription = null;
            this.loadingSubject.next(false);
        }
    }
    
    // Clean up resources when the service is no longer needed
    destroy(): void {
        this.cancel();
        this.dataSubject.next(null);
        this.errorSubject.next(null);
    }
}