export interface CursorPage<T, C> {
    content: T[];
    totalElements: number;
    hasMore: boolean;
    nextCursor: C
}