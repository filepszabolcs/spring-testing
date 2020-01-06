import { HttpClient } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { FormArray, FormControl, FormGroup, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { OrcService } from '../../services/orc.service';
import { handleValidationErrors } from '../../utils/validation.handler';

@Component({
    selector: 'app-orc-form',
    templateUrl: './orc-form.component.html',
    styleUrls: ['./orc-form.component.css'],
})
export class OrcFormComponent implements OnInit {

    orcForm: FormGroup;
    raceTypes: RaceTypeOptionModel[];
    weaponOptions: WeaponOptionModel[];
    private orcId: number;

    constructor(private orcService: OrcService, private http: HttpClient, private route: ActivatedRoute, private router: Router) {
        this.orcForm = new FormGroup({
            'name': new FormControl('', Validators.required),
            'killCount': new FormControl(null, [Validators.max(500), Validators.min(0)]),
            'raceType': new FormControl('', Validators.required),
            'weapons': new FormArray([], this.checkBoxValidator),
            'hordeId': new FormControl(null),
        });
    }

    ngOnInit() {
        this.orcService.getInitialFormData().subscribe((formInitData: FormInitDataModel) => {
            this.weaponOptions = formInitData.weapons;
            this.createCheckboxControls();
            this.raceTypes = formInitData.raceTypes;

            this.route.paramMap.subscribe(
                paramMap => {
                    const editableOrcId = paramMap.get('id');
                    if (editableOrcId) {
                        this.orcId = +editableOrcId;
                        this.getOrcDetails(editableOrcId);
                    }
                },
                error => console.warn(error),
            );
        });
    }

    getOrcDetails = (id: string) => {
        this.orcService.fetchOrcDetails(id).subscribe(
            (response: OrcFormDataModel) => {
                this.orcForm.patchValue({
                    name: response.name,
                    killCount: response.killCount,
                    raceType: response.raceType,
                    weapons: this.createWeaponsFormArray(response.weapons),
                });
            },
        );
    };

    createWeaponsFormArray = (weaponsNames: string[]) => {
        return this.weaponOptions.map(weaponOption => {
            return weaponsNames.includes(weaponOption.name);
        });
    };

    private createCheckboxControls() {
        this.weaponOptions.forEach(() => {
            const control = new FormControl(false);
            (this.orcForm.controls.weapons as FormArray).push(control);
        });
    }

    private createWeaponsArrayToSend(): string[] {
        return this.orcForm.value.weapons
            .map((weapon, index) => weapon ? this.weaponOptions[index].name : null)
            .filter(weapon => weapon !== null);
    }

    onSubmit() {
        const data = {...this.orcForm.value};
        data.weapons = this.createWeaponsArrayToSend();
        this.orcId ? this.updateOrc(data) : this.createOrc(data);
    }

    private updateOrc(data: OrcFormDataModel) {
        this.orcService.updateOrc(data, this.orcId).subscribe(
            () => this.router.navigate(['/']),
            error => handleValidationErrors(error, this.orcForm),
        );
    }

    private createOrc(data: OrcFormDataModel) {
        this.orcService.createOrc(data).subscribe(
            () => {
                // this.orcForm.reset(); - if we decide to stay on the page
                this.router.navigate(['/']);
            },
            error => handleValidationErrors(error, this.orcForm),
        );
    }

    checkBoxValidator(arr: FormArray): { required: boolean } {
        let counter = 0;
        arr.getRawValue().forEach(value => value ? counter++ : counter);
        return counter > 3 ? {required: true} : null;
    }

}
