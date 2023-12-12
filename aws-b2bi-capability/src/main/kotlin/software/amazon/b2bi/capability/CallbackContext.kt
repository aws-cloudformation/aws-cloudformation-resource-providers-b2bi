package software.amazon.b2bi.capability

import lombok.EqualsAndHashCode
import lombok.Getter
import lombok.Setter
import lombok.ToString
import software.amazon.cloudformation.proxy.StdCallbackContext

@Getter
@Setter
@ToString
@EqualsAndHashCode(callSuper = true)
class CallbackContext : StdCallbackContext()