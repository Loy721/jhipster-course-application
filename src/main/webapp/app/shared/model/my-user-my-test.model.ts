import { IMyUser } from 'app/shared/model/my-user.model';
import { IMyTest } from 'app/shared/model/my-test.model';

export interface IMyUserMyTest {
  id?: number;
  grade?: number | null;
  myUser?: IMyUser | null;
  myTest?: IMyTest | null;
}

export const defaultValue: Readonly<IMyUserMyTest> = {};
