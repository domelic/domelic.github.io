/************************************************
 * Filename: CST8219FinalProject.cpp
 * Version: 1.0
 * Author: Damir Omelic
 * Student No.: 040 918 352
 * Course Name/Number: C++ Programming CST8219
 * Lab Sect: 303
 * Assignment Name: Simon Game
 * Due Date: December 10 2020
 * Submission Date: December 09 2020
 * Professor: Eric Torunski / Abdullah Kadri
 * Purpose: GUI game using the nana library.
 ************************************************/

#include <nana/gui.hpp>
#include <nana/gui/widgets/label.hpp>
#include <nana/gui/widgets/button.hpp>
#include <nana/gui/timer.hpp>
#include <nana/audio/player.hpp>
#include <nana/threads/pool.hpp>
#include <map>
#include <vector>
#include <iostream>

enum class Button { NewGame, Red, Green, Blue, Yellow };
enum class Note { Do, Re, Mi, Fa, Sol, La, Si, DoOctave };

int main()
{
    using namespace nana;

    form fm;

    std::map<Note, std::string> note;
    note[Note::Do] = "Do_Piano.wav";
    note[Note::Re] = "Re_Piano.wav";
    note[Note::Mi] = "Mi_Piano.wav";
    note[Note::Fa] = "Fa_Piano.wav";
    note[Note::Sol] = "Sol_Piano.wav";
    note[Note::La] = "La_Piano.wav";
    note[Note::Si] = "Si_Piano.wav";
    note[Note::DoOctave] = "Do_Octave_Piano.wav";

    audio::player player[8] = { 
        note[Note::Do], note[Note::Re], note[Note::Mi], note[Note::Fa], note[Note::Sol],
        note[Note::La], note[Note::Si], note[Note::DoOctave]
    };

    button buttons[5] = { {fm, "Start New Game"}, {fm, "Red"}, {fm, "Green"},
        {fm, "Blue"}, {fm, "Yellow"} };

    for (auto i = 0; i < std::size(buttons); ++i)
    {
        buttons[i].pushed(false);
    }

    auto highlight_button = [&](int index)
    {
        switch ((Button)index)
        {
        case Button::NewGame:
            buttons[index].bgcolor(color(128, 128, 128));
            break;
        case Button::Red:
            buttons[index].bgcolor(color(255, 128, 128));
            break;
        case Button::Green:
            buttons[index].bgcolor(color(128, 255, 128));
            break;
        case Button::Blue:
            buttons[index].bgcolor(color(128, 128, 255));
            break;
        case Button::Yellow:
            buttons[index].bgcolor(color(255, 255, 128));
            break;
        default:
            break;
        }
    };

    auto draw_button = [&](int index)
    {
        switch ((Button)index)
        {
        case Button::NewGame:
            buttons[index].bgcolor(color(211, 211, 211));
            break;
        case Button::Red:
            buttons[index].bgcolor(color(255, 0, 0));
            break;
        case Button::Green:
            buttons[index].bgcolor(color(0, 255, 0));
            break;
        case Button::Blue:
            buttons[index].bgcolor(color(0, 0, 255));
            break;
        case Button::Yellow:
            buttons[index].bgcolor(color(255, 255, 0));
            break;
        default:
            break;
        }
    };

    threads::pool pool(1);

    std::vector<int> computer_move;
    std::vector<int> player_move;

    auto compare_vectors = [&]()
    {
        for (auto it1 = player_move.begin(), it2 = computer_move.begin()
            ; it1 != player_move.end()
            ; ++it1, ++it2)
        {
            if (*it1 != *it2)
            {
                return false;
            }
        }

        return true;
    };

    timer move_time(std::chrono::milliseconds(5000));
	timer timer(std::chrono::milliseconds(500));
    int random_move;
	static int itr = 1;

    auto end_game = [&]()
    {
        msgbox mb(fm, "Game Over", msgbox::yes_no);
        mb.icon(mb.icon_question);
		mb << "Sorry, wrong colour. You made it to round " << std::size(computer_move) - 1;
		mb << " of the game.\n\nPlay again?\n";
        player[(int)Note::Si].play();
        player[(int)Note::Sol].play();
        player[(int)Note::Do].play();
        if (mb.show() == msgbox::pick_yes)
        {
			buttons[(int)Button::NewGame].pushed(false);
			buttons[(int)Button::NewGame].enabled(true);
			timer.reset();
			itr = 1;
        }
        else
        {
            fm.close();
            API::exit_all();
        }
    };

    move_time.elapse([&]() 
	{ 
        move_time.stop();
		end_game(); 
	});

    auto start_new_game = [&]()
    {
        player_move.clear();
        computer_move.clear();
        buttons[(int)Button::NewGame].pushed(true);
        buttons[(int)Button::NewGame].enabled(false);
    };

	srand(time(NULL));

	auto play_computer_move = [&]()
	{
		random_move = 1 + rand() % (std::size(buttons) - 1);
		computer_move.push_back(random_move);

        timer.elapse([=, &player, &timer, &move_time]() {
            if (itr == computer_move.size())
            {
                for (auto val : computer_move)
                {
                    highlight_button(val);
                    player[val].play();
                    draw_button(val);
                }
				move_time.interval(std::chrono::milliseconds(500 * (9 + itr)));
                ++itr;
            }
            timer.stop();
		});
        timer.start();
	};

    auto buttons_disable = [&]()
    {
		for (int i = 1; i < std::size(buttons); ++i)
		{
			buttons[i].events().mouse_enter([=] {
				draw_button(i);
			});
		}
    };

    auto buttons_enable = [&]()
    {
        for (int i = 1; i < std::size(buttons); ++i)
        {
			buttons[i].events().mouse_enter([=] {
				highlight_button(i);
			});
        }
    };

    auto initialize = [=, &buttons, &player, &player_move, &computer_move, &move_time]()
    {
        for (auto i = 0; i < std::size(buttons); ++i)
        {
            draw_button(i);

            buttons[i].events().mouse_leave([=] {
                draw_button(i);
			});

            buttons[i].events().mouse_enter([=] {
                highlight_button(i);
			});

            buttons[i].events().click([=, &buttons, &player, &player_move, &computer_move, &move_time] {
                move_time.stop();
                draw_button(i);
                switch ((Button)i)
                {
                case Button::NewGame:
					start_new_game();
                    buttons_disable();
                    play_computer_move();
                    buttons_enable();
                    break;
                default:
					player[i].play();
                    if (buttons[(int)Button::NewGame].pushed())
                    {
						player_move.push_back(i);
                        if (!compare_vectors())
                        {
                            end_game();
                        }
                        else if (player_move.size() == computer_move.size())
                        {
                            buttons_disable();
							player_move.clear();
							play_computer_move();
                            buttons_enable();
                        }
                    }
                    break;
				}
                move_time.start();
			});
        }
    };

    auto draw = [&]()
    {
        fm.div("vert <<Green><Red>> <<NewGame>> <<Yellow><Blue>>");
        fm["NewGame"] << buttons[(int)Button::NewGame];
        fm["Red"] << buttons[(int)Button::Red];
        fm["Green"] << buttons[(int)Button::Green];
        fm["Blue"] << buttons[(int)Button::Blue];
        fm["Yellow"] << buttons[(int)Button::Yellow];
        fm.collocate();
        fm.show();
    };

    initialize();
    draw();

    exec();
}
