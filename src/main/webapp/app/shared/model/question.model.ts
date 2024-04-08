import { IMyTest } from 'app/shared/model/my-test.model';

export interface IQuestion {
  id?: number;
  content?: string | null;
  myTest?: IMyTest | null;
}

export const defaultValue: Readonly<IQuestion> = {};
