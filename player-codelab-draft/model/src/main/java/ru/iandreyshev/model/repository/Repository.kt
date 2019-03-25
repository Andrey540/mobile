package ru.iandreyshev.model.repository

import ru.iandreyshev.model.R

class Repository : IRepository {

    private val mSongs: List<Song> = listOf(
        Song(
            0,
            "Grand Theft Auto Vice City - Mission completed",
            R.raw.mission_completed,
            "https://farm1.staticflickr.com/92/231227933_6628a5d1fb_z.jpg?zz=1"
        ),
        Song(
            1,
            "Bomfunk Mc's - Freestyler",
            R.raw.freestyler,
            "https://otvet.imgsmail.ru/download/87156115_e772f60c21009c18906ae8f5ef02407b_800.jpg"
        )
    )

    override fun getAllSongs(): List<ISong> {
        return mSongs
    }

    override fun getSongById(id: Long): ISong? {
        return mSongs.first { song -> song.id == id }
    }

}
