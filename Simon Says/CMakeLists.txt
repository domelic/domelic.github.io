# CMakeList.txt : CMake project for CST8219FinalProject, include source and define
# project specific logic here.
#
cmake_minimum_required (VERSION 3.8)

project ("CST8219FinalProject")

# Add source to this project's executable.
add_executable (CST8219FinalProject "CST8219FinalProject.cpp" "CST8219FinalProject.h")
add_definitions(-DNANA_ENABLE_AUDIO)

add_definitions(-D_SILENCE_EXPERIMENTAL_FILESYSTEM_DEPRECATION_WARNING)
target_include_directories(CST8219FinalProject PUBLIC "C:\\nana\\include")
target_link_directories(CST8219FinalProject PUBLIC "C:\\nana\\build\\bin")
target_link_libraries(CST8219FinalProject "nana_v142_Debug_x64")

set(CMAKE_CXX_FLAGS_RELEASE "${CMAKE_CXX_FLAGS_RELEASE} /MT")
set(CMAKE_CXX_FLAGS_DEBUG "${CMAKE_CXX_FLAGS_DEBUG} /MTd")

# TODO: Add tests and install targets if needed.
