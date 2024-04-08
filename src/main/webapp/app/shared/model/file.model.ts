export interface IFile {
  id?: number;
  topic?: string | null;
  content?: string | null;
}

export const defaultValue: Readonly<IFile> = {};
