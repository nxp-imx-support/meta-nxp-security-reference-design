# Overrides for HAB and AHAB
OVERRIDES:append:mx6-generic-bsp = ":hab4"
OVERRIDES:append:mx7-generic-bsp = ":hab4"
OVERRIDES:append:mx8m-generic-bsp = ":hab4"
OVERRIDES:append:mx8ulp-generic-bsp = ":ahab"
OVERRIDES:append:mx8qm-generic-bsp = ":ahab"
OVERRIDES:append:mx8qxp-generic-bsp = ":ahab"
OVERRIDES:append:mx8dxl-generic-bsp = ":ahab"
OVERRIDES:append:mx9-generic-bsp = ":ahab"

# Select signed target based on device
SIGNED_TARGET:mx6-generic-bsp = "u-boot"
SIGNED_TARGET:mx7-generic-bsp = "u-boot"
SIGNED_TARGET:mx8m-generic-bsp = "flash_evk"
SIGNED_TARGET:mx8ulp-generic-bsp = "flash_singleboot_m33"
SIGNED_TARGET:mx8qm-generic-bsp = "flash_spl"
SIGNED_TARGET:mx8qxp-generic-bsp = "flash_spl"
SIGNED_TARGET:mx8dxl-generic-bsp = "flash"
SIGNED_TARGET:mx93-generic-bsp = "flash_singleboot"
