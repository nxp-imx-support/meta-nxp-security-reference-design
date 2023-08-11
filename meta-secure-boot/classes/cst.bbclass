do_configure:prepend () {
    # Check if CST_PATH is set and CST binary is available
    if [[ -z "${CST_PATH}" && ! -e "${CST_PATH}/linux64/bin/cst" ]]; then
        bbfatal 'Code-Signing tool (CST) is not installed.
        Make sure it is in your PATH or edit you configuration file
        and set CST_PATH variable to the top directory of CST'
    fi
}
