import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { OrcService } from '../../services/orc.service';

@Component({
    selector: 'app-orc-list',
    templateUrl: './orc-list.component.html',
    styleUrls: ['./orc-list.component.css'],
})
export class OrcListComponent implements OnInit {

    orcs: OrcListItemModel[] = [];

    constructor(private orcService: OrcService, private router: Router) {}

    ngOnInit() {
        this.fetchOrcs();
    }

    fetchOrcs = () => {
        this.orcService.fetchOrcs().subscribe((orcList: OrcListItemModel[]) => {
            this.orcs = orcList;
        });
    };

    deleteOrc(id: number) {
        this.orcService.deleteOrc(id).subscribe(
            (response: OrcListItemModel[]) => {
                this.orcs = response;
            },
            error => console.warn(error),
        );
    }

    editOrc = (id: number) => {
        this.router.navigate(['/orc-form/', id]);
    };


}
