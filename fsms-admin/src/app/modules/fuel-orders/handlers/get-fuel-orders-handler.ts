import { catchError, Observable, throwError } from "rxjs";
import { FuelOrder, FuelOrderRestClient } from "fsms-web-api";
import { CommandHandler } from "../../common/command-handler";
import { inject, Injectable } from "@angular/core";
import { MessageService } from "primeng/api";
import { GetFuelOrders } from "../fuel-order-commands";

@Injectable({ providedIn: "root" })
export class GetFuelOrdersHandler extends CommandHandler<GetFuelOrders, FuelOrder[]> {

    private readonly api = inject(FuelOrderRestClient); 

    private readonly messageService = inject(MessageService);

    execute(_: GetFuelOrders): Observable<FuelOrder[]> {
        return this.api.getFuelOrders().pipe(
            catchError((e) => {
                this.messageService.add({ 
                    severity: "error", 
                    summary: "Error", 
                    detail: "An error occurred while fetching fuel orders"
                })
                return throwError(() => e);
            }),
        );
    }

}