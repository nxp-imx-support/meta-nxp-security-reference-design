do_configure:prepend () {
    # Check if SIG_TOOL_PATH is set and CST/SPSDK binary is available
    if [ -z "${SIG_TOOL_PATH}" ] ||
       { [ ! -e "${SIG_TOOL_PATH}/linux64/bin/cst" ] &&
         [ ! -e "${SIG_TOOL_PATH}/spsdk" ]; }; then
        bbfatal 'Code-Signing tool (CST) or (Secure Provisioning SDK) SPSDK is
        not installed. Edit local.conf file and set SIG_TOOL_PATH variable to 
        the top directory of CST/SPSDK installation'
    fi

    # HAB images cannot be signed with SPSDK tool
    if [ -e "${SIG_TOOL_PATH}/spsdk" ] &&
       { [[ "${MACHINE}" =~ ^imx6  ]] ||
         [[ "${MACHINE}" =~ ^imx7  ]] ||
         [[ "${MACHINE}" =~ ^imx8m ]]; }; then
        bbfatal 'Signing using SPSDK tool is not supported with ${MACHINE}'
    fi

    # AHAB images cannot be signed with CST tool
    if [ -e "${SIG_TOOL_PATH}/linux64/bin/cst" ] &&
       { [[ "${MACHINE}" =~ ^imx8d  ]] ||
         [[ "${MACHINE}" =~ ^imx8q  ]] ||
         [[ "${MACHINE}" =~ ^imx8ulp  ]] ||
         [[ "${MACHINE}" =~ ^imx9 ]]; }; then
        bbfatal 'Signing using CST tool is not supported with ${MACHINE}'
    fi

    # If not set in local.conf, SIG_DATA_PATH is set to SIG_TOOL_PATH. Same is
    # expected by signer tool
    if [ -z "${SIG_DATA_PATH}" ]; then
        bbnote 'SIG_DATA_PATH not set. SIG_TOOL_PATH is used'
        SIG_DATA_PATH=${SIG_TOOL_PATH}
    fi

}