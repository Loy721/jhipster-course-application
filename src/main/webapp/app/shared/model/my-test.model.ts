import { IFile } from 'app/shared/model/file.model';

export interface IMyTest {
  id?: number;
  name?: string | null;
  file?: IFile | null;
}

export const defaultValue: Readonly<IMyTest> = {};
