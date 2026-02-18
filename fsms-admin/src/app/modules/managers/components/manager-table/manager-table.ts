import { NgTemplateOutlet } from "@angular/common";
import { Component, computed, input, TemplateRef } from "@angular/core";
import { Manager, ManagerStatus } from "fsms-web-api";
import { SkeletonModule } from "primeng/skeleton";
import { TableModule } from "primeng/table";
import { TagModule } from "primeng/tag";
import { ManagerTemplateDirective } from "../../directives/manager-template-directive";

@Component({
    selector: 'app-manager-table',
    imports: [NgTemplateOutlet, TableModule, TagModule, SkeletonModule, ManagerTemplateDirective],
    templateUrl: './manager-table.html'
})
export class ManagerTable {
    managers = input<Manager[]>([]);
    loading = input<boolean>(false);
    showStatus = input<boolean>(false);
    actionsTemplate = input.required<TemplateRef<{ $implicit: Manager }>>();

    protected readonly skeletonRows = new Array(5).fill(null);
    protected readonly skeletonCols = computed(() => new Array(this.showStatus() ? 6 : 5).fill(null));

    protected getStatusSeverity(manager: Manager): 'success' | undefined {
        return manager.status === ManagerStatus.Active ? 'success' : undefined;
    }
}
