import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { OrcFormComponent } from './components/orc-form/orc-form.component';
import { OrcListComponent } from './components/orc-list/orc-list.component';


const routes: Routes = [
    {path: 'orc-list', component: OrcListComponent},
    {path: 'orc-form', component: OrcFormComponent},
    {path: 'orc-form/:id', component: OrcFormComponent},
    {path: '', redirectTo: 'orc-list', pathMatch: 'full'},
];

@NgModule({
    imports: [RouterModule.forRoot(routes)],
    exports: [RouterModule],
})
export class AppRoutingModule {
}
