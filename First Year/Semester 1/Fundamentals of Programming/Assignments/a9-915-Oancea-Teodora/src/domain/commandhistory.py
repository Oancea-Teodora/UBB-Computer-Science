
class CommandHistory:
    def __init__(self, command_type: str, parameters: []):
        self._command_type = command_type
        self._parameters = parameters

    @property
    def command_type(self):
        return self._command_type

    @property
    def parameters(self):
        return  self._parameters