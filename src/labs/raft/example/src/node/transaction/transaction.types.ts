export type Operations = 'Set' | 'Get' | 'Del';
export type CommandOperations = Exclude<Operations, 'Get'>;
